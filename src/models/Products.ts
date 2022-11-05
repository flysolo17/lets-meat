import { QueryDocumentSnapshot } from "firebase/firestore";

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
  createdAt: number;
}

export const classroomConveter = {
  toFirestore: (data: Products) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => snap.data() as Products,
};
