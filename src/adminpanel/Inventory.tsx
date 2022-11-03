import { Container, Box, Stack } from "@mui/material";
import AddInventoryPage from "../components/AddInventory";
import { experimentalStyled as styled } from "@mui/material/styles";
import React, { useEffect, useState } from "react";
import Paper from "@mui/material/Paper";
import Grid from "@mui/material/Grid";
import ProductCard from "../components/ProductCard";
import {
  collection,
  doc,
  onSnapshot,
  orderBy,
  query,
  where,
} from "firebase/firestore";
import { useAuth } from "../auth/AuthContext";
import { firestore } from "../config/config";
import { PRODUCTS_TABLE } from "../utils/Constants";
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
    <Box sx={{ width: "100%", padding: 2 }}>
      <Stack direction={"column"} spacing={2}>
        <Box
          sx={{
            alignItems: "end",
            display: "flex",
            justifyContent: "end",
          }}
        >
          <AddInventoryPage />
        </Box>

        <Grid
          container
          spacing={{ xs: 2, md: 3 }}
          columns={{ xs: 4, sm: 8, md: 12 }}
        >
          {product.map((data) => (
            <Grid item xs={2} sm={4} md={4} key={data.id} sx={{ flexGrow: 1 }}>
              <ProductCard product={data} />
            </Grid>
          ))}
        </Grid>
      </Stack>
    </Box>
  );
};

export default InventoryPage;
