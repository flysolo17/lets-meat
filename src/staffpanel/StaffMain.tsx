import { Box, Container, List, Stack, Typography } from "@mui/material";
import { experimentalStyled as styled } from "@mui/material/styles";
import React, { useState, useEffect } from "react";
import Paper from "@mui/material/Paper";
import Grid from "@mui/material/Grid";
import { useAuth } from "../auth/AuthContext";
import { collection, doc, onSnapshot } from "firebase/firestore";
import { firestore } from "../config/config";
import { PRODUCTS_TABLE, STAFF_ID, STAFF_TABLE } from "../utils/Constants";
import ProductCard from "../components/ProductCard";
import { Staff, staffConverter } from "../models/Staff";

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
  ...theme.typography.body2,
  padding: theme.spacing(2),
  textAlign: "center",
  color: theme.palette.text.secondary,
}));
interface StaffMainPageProps {}

const StaffMainPage: React.FunctionComponent<StaffMainPageProps> = () => {
  const [product, setProduct] = useState<any[]>([]);
  const { currentUser } = useAuth();
  const [staff, setStaff] = useState<Staff>({} as Staff);
  useEffect(() => {
    const id = localStorage.getItem(STAFF_ID);
    console.log(id);
    if (id != null) {
      const ref = doc(firestore, STAFF_TABLE, id).withConverter(staffConverter);
      const unsub = onSnapshot(ref, (snapshot) => {
        if (snapshot.exists()) {
          let data = snapshot.data();
          setStaff(data);
          console.log("staff info");
        }
      });
      return () => unsub();
    }
  }, []);
  useEffect(() => {
    if (currentUser != null) {
      const ref = collection(firestore, PRODUCTS_TABLE);
      const unsub = onSnapshot(ref, (snapshot) => {
        let data: any[] = [];
        snapshot.forEach((doc) => {
          data.push({ ...doc.data(), id: doc.id });
        });
        setProduct(data);
        console.log("product fetch successful!");
      });
      return () => unsub();
    }
  }, []);
  return (
    <Stack
      sx={{
        width: "100%",
        padding: 1,
        backgroundColor: "#FBFBFE",
        height: "100vh",
      }}
      spacing={1}
      direction={"row"}
    >
      <Box
        sx={{
          width: "70%",
          height: "100%",
          overflow: "scroll",
        }}
      >
        <Grid
          container
          spacing={{ xs: 2, md: 3 }}
          columns={{ xs: 4, sm: 8, md: 12 }}
        >
          {Array.from(Array(100)).map((_, index) => (
            <Grid item xs={2} sm={4} md={4} key={index}>
              <Item>xs=2</Item>
            </Grid>
          ))}
        </Grid>
      </Box>

      <Paper
        sx={{
          width: "30%",
          height: "100%",
        }}
        elevation={1}
      ></Paper>
    </Stack>
  );
};

export default StaffMainPage;
