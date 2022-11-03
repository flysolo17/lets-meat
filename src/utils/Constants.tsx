
export const PRODUCT_STORAGE: string = "products";
export const PRODUCTS_TABLE = "Products";

export const getTimestamp = (): number => {
  return new Date().getTime() / 1000;
};
