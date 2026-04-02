import { useStore } from '@nanostores/react';
import { favoriteItems, removeFromFavorites } from '../../store/favoriteStore';
import { toast } from 'sonner';

export default function FavoritesList() {
    const favorites = useStore(favoriteItems);

    const formatPrice = (price: number) => {
        const numericPrice = Number(price) || 0;
        if (numericPrice === 0) return 'Gratis';
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'EUR',
        }).format(numericPrice);
    };

    const handleRemove = async (id: string, name: string) => {
        const result = await removeFromFavorites(id);
        if (result.success) {
            toast.info(`${name} eliminado de favoritos`);
        } else {
            toast.error(result.message);
        }
    };

    if (favorites.length === 0) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[50vh] text-center p-4">
                <div className="w-24 h-24 bg-base-200 rounded-full flex items-center justify-center mb-6">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-base-content/50"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z" /></svg>
                </div>
                <h2 className="text-2xl font-bold mb-2">Aún no tienes favoritos</h2>
                <p className="text-base-content/70 mb-6 max-w-md">
                    Pulsa el icono del corazón en cualquier producto para guardarlo aquí.
                </p>
                <a href="/" className="btn btn-primary">
                    Empezar a comprar
                </a>
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {favorites.map((product) => (
                <div key={product.id} className="card bg-base-100 shadow-sm hover:shadow-xl transition-shadow duration-300 border border-base-200 overflow-hidden relative group">
                    <button
                        type="button"
                        onClick={(event) => {
                            event.preventDefault();
                            event.stopPropagation();
                            void handleRemove(product.id, product.name);
                        }}
                        className="absolute top-3 right-3 z-20 btn btn-circle btn-sm bg-white hover:bg-error hover:text-white border-none shadow-md opacity-100 sm:opacity-0 sm:group-hover:opacity-100 transition-opacity"
                        aria-label="Eliminar de favoritos"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M18 6 6 18" /><path d="m6 6 12 12" /></svg>
                    </button>

                    <a href={`/product/view?id=${product.id}`} className="block relative aspect-4/5 bg-gray-100 overflow-hidden">
                        <img
                            src={product.image}
                            alt={product.name}
                            className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                        />
                    </a>

                    <div className="p-4">
                        <h3 className="font-bold text-lg mb-1 truncate">
                            <a href={`/product/view?id=${product.id}`} className="hover:text-primary transition-colors">
                                {product.name}
                            </a>
                        </h3>

                        <div className="flex items-center justify-between mt-4">
                            <span className="text-xl font-bold text-primary">
                                {formatPrice(product.price)}
                            </span>
                            <a
                                href={`/product/view?id=${product.id}`}
                                className="btn btn-circle btn-sm btn-primary text-primary-content shadow-md"
                                aria-label="Ver producto"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
                            </a>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}
