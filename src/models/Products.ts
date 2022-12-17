import { QueryDocumentSnapshot } from "firebase/firestore";
import { Comments } from "./Comments";

export interface Products {
  code: string;
  userID: string;
  images: string;
  productName: string;
  cost: number;
  price: number;
  quantity: number;
  description: string;
  details: string;
  isAvailable: boolean;
  comments: Comments[];
  createdAt: number;
  expiration: number;
  category: string;
  weight: number;
}


export const classroomConveter = {
  toFirestore: (data: Products) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => snap.data() as Products,
};
