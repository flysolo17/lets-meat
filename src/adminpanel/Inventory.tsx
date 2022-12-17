import { Container, Box, Stack, Typography } from "@mui/material";
import AddInventoryPage from "../dialog/AddInventory";

import React, { useEffect, useState } from "react";

import Divider from "@mui/material/Divider";

import InventoryTable from "./InventoryTable";
import { useAuth } from "../auth/AuthContext";
import { collection, onSnapshot, orderBy, query, where } from "firebase/firestore";
import { getAllCategories, PRODUCTS_TABLE } from "../utils/Constants";
import { firestore } from "../config/config";
interface InventoryPageProps {}

const InventoryPage: React.FunctionComponent<InventoryPageProps> = () => {
    const [product, setProduct] = useState<any[]>([]);
    const { currentUser } = useAuth();
    useEffect(() => {
      if (currentUser != null) {
        const PRODUCTS_REF = collection(firestore, PRODUCTS_TABLE);
        const PRODUCTS_QUERY = query(
          PRODUCTS_REF,
          where("userID", "==", currentUser.uid),
          orderBy("createdAt", "desc")
        );
        const unsub = onSnapshot(PRODUCTS_QUERY, (snapshot) => {
          let data: any = [];
          snapshot.forEach((doc) => {
            data.push({ ...doc.data(), id: doc.id });
          });
          setProduct(data);
          console.log(data);
        });
        return () => unsub();
      }
    }, []);
  return (
    <Box
      sx={{
        width: "100%",
        padding: 2,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Stack direction={"column"} spacing={2} sx={{ width: "100%" }}>
        <Box
          sx={{
            flexGrow: 1,
            alignItems: "center",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <Typography
            sx={{
              fontFamily: "Poppins",
              fontWeight: 400,
              fontSize: 30,
              fontStyle: "normal",
            }}
          >
            Inventory
          </Typography>
          <AddInventoryPage categories={getAllCategories(product)}/>
        </Box>
        <Divider />
        <InventoryTable product={product}/>
      </Stack>
    </Box>
  );
};

export default InventoryPage;
