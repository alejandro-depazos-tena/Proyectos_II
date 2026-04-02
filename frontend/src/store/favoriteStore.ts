import { atom, computed } from 'nanostores';

export interface FavoriteItem {
    id: string;
    name: string;
    price: number;
    image: string;
    stock: number;
    slug: string;
}

interface FavoriteActionResult {
    success: boolean;
    message: string;
}

const SESSION_TOKEN_KEY = 'ufvshares_token';
const SESSION_ID_KEY = 'ufvshares_id';
const SESSION_EMAIL_KEY = 'ufvshares_email';
const isBrowser = typeof window !== 'undefined' && typeof localStorage !== 'undefined';
const API_BASE = (import.meta.env.PUBLIC_API_BASE ?? 'http://localhost:8080/api').replace(/\/$/, '');
const BACKEND_BASE = API_BASE.replace(/\/api$/, '');

let loadingPromise: Promise<void> | null = null;

function getSessionToken(): string | null {
    if (!isBrowser) return null;
    return localStorage.getItem(SESSION_TOKEN_KEY)?.trim() || null;
}

function resolveImageUrl(raw: unknown): string {
    if (typeof raw !== 'string' || !raw.trim()) return '/Logo.png';
    if (raw.startsWith('http://') || raw.startsWith('https://')) return raw;
    if (raw.startsWith('/')) return `${BACKEND_BASE}${raw}`;
    return raw;
}

function mapApiFavorite(item: any): FavoriteItem {
    const id = String(item?.idProducto ?? '');
    return {
        id,
        name: String(item?.titulo ?? 'Producto'),
        price: Number(item?.precio ?? 0),
        image: resolveImageUrl(item?.fotoUrl ?? item?.imagenUrl),
        stock: 1,
        slug: id,
    };
}

async function requestFavorites(): Promise<FavoriteItem[]> {
    const token = getSessionToken();
    if (!token) return [];

    const response = await fetch(`${API_BASE}/me/favoritos`, {
        headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
        if (response.status === 401) return [];
        throw new Error('No se pudieron cargar los favoritos');
    }

    const data = await response.json();
    if (!Array.isArray(data)) return [];
    return data.map(mapApiFavorite).filter((item) => item.id);
}

export const favoriteItems = atom<FavoriteItem[]>([]);

export async function refreshFavorites() {
    if (!isBrowser) return;
    loadingPromise = (async () => {
        const items = await requestFavorites();
        favoriteItems.set(items);
    })();
    try {
        await loadingPromise;
    } finally {
        loadingPromise = null;
    }
}

async function ensureFavoritesLoaded() {
    if (!isBrowser) return;
    if (loadingPromise) {
        await loadingPromise;
        return;
    }
    if (favoriteItems.get().length > 0) return;
    await refreshFavorites();
}

if (isBrowser) {
    refreshFavorites().catch(() => {
        favoriteItems.set([]);
    });

    window.addEventListener('storage', (event) => {
        if (event.key === SESSION_TOKEN_KEY || event.key === SESSION_ID_KEY || event.key === SESSION_EMAIL_KEY) {
            refreshFavorites().catch(() => {
                favoriteItems.set([]);
            });
            return;
        }
    });
}

export async function addToFavorites(product: FavoriteItem): Promise<FavoriteActionResult> {
    const token = getSessionToken();
    if (!token) {
        return { success: false, message: 'Inicia sesion para guardar favoritos.' };
    }

    const idProducto = Number(product.id);
    if (!Number.isFinite(idProducto) || idProducto <= 0) {
        return { success: false, message: 'Producto invalido.' };
    }

    const response = await fetch(`${API_BASE}/me/favoritos/${idProducto}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
        return { success: false, message: 'No se pudo guardar en favoritos.' };
    }

    const items = favoriteItems.get();
    if (!items.some((item) => item.id === product.id)) {
        favoriteItems.set([...items, product]);
    }

    return { success: true, message: `${product.name} anadido a favoritos` };
}

export async function removeFromFavorites(productId: string): Promise<FavoriteActionResult> {
    const token = getSessionToken();
    if (!token) {
        return { success: false, message: 'Inicia sesion para gestionar favoritos.' };
    }

    const idProducto = Number(productId);
    if (!Number.isFinite(idProducto) || idProducto <= 0) {
        return { success: false, message: 'Producto invalido.' };
    }

    const response = await fetch(`${API_BASE}/me/favoritos/${idProducto}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok && response.status !== 404) {
        return { success: false, message: 'No se pudo eliminar de favoritos.' };
    }

    const items = favoriteItems.get();
    const itemToRemove = items.find((item) => item.id === productId);
    const nextItems = items.filter((item) => item.id !== productId);
    favoriteItems.set(nextItems);
    return { success: true, message: `${itemToRemove?.name || 'Producto'} eliminado de favoritos` };
}

export async function toggleFavorite(product: FavoriteItem): Promise<FavoriteActionResult> {
    await ensureFavoritesLoaded();
    const items = favoriteItems.get();
    const exists = items.some((item) => item.id === product.id);

    if (exists) {
        return await removeFromFavorites(product.id);
    } else {
        return await addToFavorites(product);
    }
}

export function isFavorite(productId: string) {
    const items = favoriteItems.get();
    return items.some((item) => item.id === productId);
}

export const totalFavorites = computed(favoriteItems, (items) => items.length);
