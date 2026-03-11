export interface Seller {
  name: string;
  avatar?: string;
  degree?: string;
  rating?: number;
  totalSales?: number;
  whatsapp?: string;
  email?: string;
}

export interface Product {
  id: string;
  name: string;
  title: string;
  price: number;
  description: string;
  category: string;
  subcategory?: string;
  stock: number;
  images: string[];
  slug: string;
  badge?: string;
  discount?: number;
  specs?: { label: string; value: string }[];
  seller?: Seller;
}

export const products: Product[] = [
  {
    id: "1",
    name: "Libro de Álgebra",
    title: "Libro de Álgebra",
    price: 12,
    description: "Libro de álgebra lineal en buen estado. Ideal para 1º/2º.",
    category: "Libros",
    stock: 1,
    images: ["/foto.webp"],
    slug: "libro-algebra",
    badge: "Venta",
    specs: [
      { label: "Estado", value: "Buen estado" },
      { label: "Entrega", value: "En campus" },
    ],
    seller: {
      name: "Carlos Martínez",
      degree: "Ingeniería Informática, 3º",
      rating: 4.8,
      totalSales: 12,
      email: "carlos.martinez@ufv.es",
    },
  },
  {
    id: "2",
    name: "Libro de Economía",
    title: "Libro de Microeconomía",
    price: 10,
    description: "Edición reciente, subrayados mínimos. Perfecto para examen.",
    category: "Libros",
    stock: 1,
    images: ["/campus.webp"],
    slug: "libro-microeconomia",
    badge: "Venta",
    seller: {
      name: "Laura Sánchez",
      degree: "ADE, 2º",
      rating: 4.5,
      totalSales: 5,
      email: "laura.sanchez@ufv.es",
    },
  },
  {
    id: "5",
    name: "Calculadora científica",
    title: "Calculadora científica (alquiler)",
    price: 5,
    description: "Alquiler por semanas. Se entrega con pilas.",
    category: "Material",
    stock: 3,
    images: ["/campus.webp"],
    slug: "calculadora-cientifica",
    badge: "Alquiler",
    seller: {
      name: "Miguel Torres",
      degree: "Física, 4º",
      rating: 4.9,
      totalSales: 20,
      email: "miguel.torres@ufv.es",
    },
  },
  {
    id: "6",
    name: "Bata de laboratorio",
    title: "Bata de laboratorio (alquiler)",
    price: 4,
    description: "Talla M. Alquiler por semanas.",
    category: "Material",
    stock: 2,
    images: ["/foto.webp"],
    slug: "bata-laboratorio",
    badge: "Alquiler",
    seller: {
      name: "Ana Gómez",
      degree: "Biología, 1º",
      rating: 4.3,
      totalSales: 3,
      email: "ana.gomez@ufv.es",
    },
  },
  {
    id: "7",
    name: "Trípode para móvil",
    title: "Trípode para móvil",
    price: 6,
    description: "Útil para presentaciones y grabar prácticas.",
    category: "Tecnologia",
    stock: 4,
    images: ["/campus.webp"],
    slug: "tripode-movil",
    badge: "Venta",
    seller: {
      name: "Pablo Ruiz",
      degree: "Comunicación, 3º",
      rating: 4.6,
      totalSales: 8,
      email: "pablo.ruiz@ufv.es",
    },
  },
  {
    id: "8",
    name: "Teclado mecánico",
    title: "Teclado mecánico",
    price: 20,
    description: "Buen estado. Incluye cable y keycaps extra.",
    category: "Tecnologia",
    stock: 1,
    images: ["/foto.webp"],
    slug: "teclado-mecanico",
    badge: "Venta",
    seller: {
      name: "Pablo Ruiz",
      degree: "Comunicación, 3º",
      rating: 4.6,
      totalSales: 8,
      email: "pablo.ruiz@ufv.es",
    },
  },
  {
    id: "9",
    name: "Diccionario Inglés C1",
    title: "Diccionario Inglés (C1)",
    price: 8,
    description: "Diccionario para preparación avanzada.",
    category: "Idiomas",
    stock: 1,
    images: ["/foto.webp"],
    slug: "diccionario-ingles-c1",
    badge: "Venta",
    seller: {
      name: "Sofía Navarro",
      degree: "Traducción e Interpretación, 2º",
      rating: 5.0,
      totalSales: 7,
      email: "sofia.navarro@ufv.es",
    },
  },
  {
    id: "10",
    name: "Libro de Francés",
    title: "Libro de Francés",
    price: 9,
    description: "Libro + cuaderno de ejercicios (sin completar).",
    category: "Idiomas",
    stock: 1,
    images: ["/campus.webp"],
    slug: "libro-frances",
    badge: "Venta",
    seller: {
      name: "Sofía Navarro",
      degree: "Traducción e Interpretación, 2º",
      rating: 5.0,
      totalSales: 7,
      email: "sofia.navarro@ufv.es",
    },
  },
  {
    id: "11",
    name: "Balón de fútbol",
    title: "Balón de fútbol",
    price: 7,
    description: "Para pachangas en el campus.",
    category: "Deporte",
    stock: 1,
    images: ["/campus.webp"],
    slug: "balon-futbol",
    badge: "Venta",
    seller: {
      name: "Diego Morales",
      degree: "Ciencias del Deporte, 1º",
      rating: 4.4,
      totalSales: 2,
      email: "diego.morales@ufv.es",
    },
  },
  {
    id: "12",
    name: "Raqueta de tenis (alquiler)",
    title: "Raqueta de tenis (alquiler)",
    price: 3,
    description: "Alquiler por días/semanas. Buen agarre.",
    category: "Deporte",
    stock: 2,
    images: ["/foto.webp"],
    slug: "raqueta-tenis",
    badge: "Alquiler",
    seller: {
      name: "Diego Morales",
      degree: "Ciencias del Deporte, 1º",
      rating: 4.4,
      totalSales: 2,
      email: "diego.morales@ufv.es",
    },
  },
];

export function getRelatedProducts(
  category: string,
  currentSlug: string,
  limit = 4
): Product[] {
  return products
    .filter((p) => p.category === category && p.slug !== currentSlug)
    .slice(0, limit);
}
