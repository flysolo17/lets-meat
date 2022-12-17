import { Contacts } from "./Contacts";

export interface Address {
  contacts: Contacts;
  addressLine: string;
  postalCode: number;
  street: String;
  
}
