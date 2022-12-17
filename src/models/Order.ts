import { StringLiteralLike } from "typescript"
import { Address } from "./Address";
import { OrderItems } from "./OrderItems";
import { OrderStatus } from "./OrderStatus";

export interface Order {
    clientID: string;
    orderNumber : string;
    address: Address;
    items: OrderItems[];
    message : string
    status : OrderStatus
    date : number
}