import { atom, computed } from 'nanostores';

export interface FavoriteItem {
    id: string;
    name: string;
    price: number;
    image: string;
    stock: number;
    slug: string;
}

const LEGACY_STORAGE_KEY = 'favorite_items';
const STORAGE_PREFIX = 'favorite_items:';
const SESSION_ID_KEY = 'ufvshares_id';
const SESSION_EMAIL_KEY = 'ufvshares_email';
const isBrowser = typeof window !== 'undefined' && typeof localStorage !== 'undefined';

function safeParseFavorites(raw: string | null): FavoriteItem[] {
    if (!raw) return [];
    try {
        const parsed = JSON.parse(raw);
        return Array.isArray(parsed) ? parsed : [];
    } catch {
        return [];
    }
}

function resolveUserScope(): string {
    if (!isBrowser) return 'guest';

    const userId = localStorage.getItem(SESSION_ID_KEY)?.trim();
    if (userId) return `id:${userId}`;

    const email = localStorage.getItem(SESSION_EMAIL_KEY)?.trim().toLowerCase();
    if (email) return `email:${email}`;

    return 'guest';
}

function storageKey(scope: string): string {
    return `${STORAGE_PREFIX}${scope}`;
}

function loadFavoritesForScope(scope: string): FavoriteItem[] {
    if (!isBrowser) return [];

    const scopedKey = storageKey(scope);
    const scoped = safeParseFavorites(localStorage.getItem(scopedKey));
    if (scoped.length > 0) return scoped;

    // One-time migration from old global key to the current scope.
    const legacy = safeParseFavorites(localStorage.getItem(LEGACY_STORAGE_KEY));
    if (legacy.length > 0) {
        localStorage.setItem(scopedKey, JSON.stringify(legacy));
        return legacy;
    }

    return [];
}

let currentScope = resolveUserScope();
export const favoriteItems = atom<FavoriteItem[]>(loadFavoritesForScope(currentScope));

function ensureScopeIsSynced() {
    if (!isBrowser) return;

    const nextScope = resolveUserScope();
    if (nextScope === currentScope) return;

    const previousScope = currentScope;
    const previousItems = favoriteItems.get();

    currentScope = nextScope;
    const nextItems = loadFavoritesForScope(currentScope);

    // Keep favorites when session identity is upgraded from email to user id.
    if (
        previousScope.startsWith('email:') &&
        currentScope.startsWith('id:') &&
        nextItems.length === 0 &&
        previousItems.length > 0
    ) {
        favoriteItems.set(previousItems);
        persistCurrentScope(previousItems);
        return;
    }

    favoriteItems.set(nextItems);
}

function persistCurrentScope(items: FavoriteItem[]) {
    if (!isBrowser) return;
    localStorage.setItem(storageKey(currentScope), JSON.stringify(items));
}

if (isBrowser) {
    window.addEventListener('storage', (event) => {
        if (event.key === SESSION_ID_KEY || event.key === SESSION_EMAIL_KEY) {
            ensureScopeIsSynced();
            return;
        }

        if (event.key === storageKey(currentScope)) {
            favoriteItems.set(safeParseFavorites(event.newValue));
        }
    });
}

export function addToFavorites(product: FavoriteItem) {
    ensureScopeIsSynced();
    const items = favoriteItems.get();
    const existingItem = items.find((item) => item.id === product.id);

    if (!existingItem) {
        const nextItems = [...items, product];
        favoriteItems.set(nextItems);
        persistCurrentScope(nextItems);
        return { success: true, message: `${product.name} añadido a favoritos` };
    }
    return { success: false, message: `${product.name} ya está en favoritos` }; // Should not happen if we toggle
}

export function removeFromFavorites(productId: string) {
    ensureScopeIsSynced();
    const items = favoriteItems.get();
    const itemToRemove = items.find(item => item.id === productId);
    const nextItems = items.filter((item) => item.id !== productId);
    favoriteItems.set(nextItems);
    persistCurrentScope(nextItems);
    return { success: true, message: `${itemToRemove?.name || 'Producto'} eliminado de favoritos` };
}

export function toggleFavorite(product: FavoriteItem) {
    ensureScopeIsSynced();
    const items = favoriteItems.get();
    const exists = items.some((item) => item.id === product.id);

    if (exists) {
        return removeFromFavorites(product.id);
    } else {
        return addToFavorites(product);
    }
}

export function isFavorite(productId: string) {
    ensureScopeIsSynced();
    const items = favoriteItems.get();
    return items.some((item) => item.id === productId);
}

export const totalFavorites = computed(favoriteItems, (items) => items.length);
