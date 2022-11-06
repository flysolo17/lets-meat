export const PRODUCT_STORAGE: string = "products";
export const STAFF_STORAGE: string = "staff";
export const PRODUCTS_TABLE = "Products";
export const STAFF_TABLE = "Staff";
export const getTimestamp = (): number => {
  return new Date().getTime() / 1000;
};
