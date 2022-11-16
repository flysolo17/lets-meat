import { QueryDocumentSnapshot } from "firebase/firestore";

export interface Staff {
  profile: string;
  displayName: string;
  fullname: string;
  role: string;
  contactNumber: string;
  pin: string;
  employedAt: number;
  
}
export const staffConverter = {
  toFirestore: (data: Staff) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => snap.data() as Staff,
};