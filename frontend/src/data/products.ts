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
  },
  {
    id: "3",
    name: "Apuntes de Programación",
    title: "Apuntes de Programación",
    price: 8,
    description: "PDF + impresos. Incluye ejercicios resueltos.",
    category: "Apuntes",
    stock: 10,
    images: ["/foto.webp"],
    slug: "apuntes-programacion",
    badge: "Venta",
  },
  {
    id: "4",
    name: "Apuntes de Estadística",
    title: "Apuntes de Estadística",
    price: 6,
    description: "Resumen + problemas tipo examen.",
    category: "Apuntes",
    stock: 6,
    images: ["/campus.webp"],
    slug: "apuntes-estadistica",
    badge: "Venta",
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
