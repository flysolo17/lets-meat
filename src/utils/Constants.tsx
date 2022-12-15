export const PRODUCT_STORAGE: string = "products";
export const STAFF_STORAGE: string = "staff";
export const PRODUCTS_TABLE = "Products";
export const STAFF_TABLE = "Staff";
export const STAFF_ID = "staffID";
export const getTimestamp = (): number => {
  return new Date().getTime() / 1000;
};
export const STAFF_ROLE = [
  { value: "CASHIER", label: "CASHIER" },
  { value: "OTHER", label: "OTHER" },
];

export const posBackground = (isAvailable: boolean): string => {
  let color: string = "";
  if (!isAvailable) {
    color = "#FF9D83";
  }
  return color;
};
