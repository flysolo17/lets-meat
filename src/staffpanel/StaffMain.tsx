import {
  Box,
  Container,
  List,
  Stack,
  Typography,
  Divider,
  ListItemText,
  ListItemButton,
  ListItem,
} from "@mui/material";
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
import PosProductCard from "./components/PosProductCard";
import { FixedSizeList, ListChildComponentProps } from "react-window";
interface StaffMainPageProps {}
function renderRow(props: ListChildComponentProps) {
  const { index, style } = props;
  return (
    <ListItem style={style} key={index} component="div" disablePadding>
      <ListItemButton>
        <ListItemText primary={`Item ${index + 1}`} />
      </ListItemButton>
    </ListItem>
  );
}
const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: "center",
  color: theme.palette.text.secondary,
}));
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
          width: "75%",
          height: "100%",
          overflow: "auto",
          padding: 1,
        }}
      >
        <Typography
          sx={{
            fontFamily: "Poppins",
            fontSize: 30,
            fontWeight: 700,
            fontStyle: "normal",
          }}
        >
          {staff.fullname}
        </Typography>
        <Typography
          sx={{
            fontFamily: "Poppins",
            fontSize: 20,
            fontWeight: 400,
            fontStyle: "normal",
          }}
        >
          Cashier
        </Typography>
        <Divider
          sx={{
            marginY: 2,
          }}
        />
        <Grid
          container
          spacing={{ xs: 2, md: 3 }}
          columns={{ xs: 4, sm: 8, md: 12 }}
        >
          {product.map((row) => (
            <Grid item xs={2} sm={4} md={4} key={row.id}>
              <PosProductCard product={row} />
            </Grid>
          ))}
        </Grid>
      </Box>

      <Box
        sx={{
          width: "25%",
          height: "100%",
        }}
      >
        <List
          sx={{
            width: "100%",
            height: "80%",
            bgcolor: "background.paper",
            overflow: "auto",
            scrollbarWidth: "none",
          }}
        >
          {Array.from(Array(100)).map((_, index) => (
            <>
              <ListItem key={index}>
                <ListItemText primary="Photos" secondary="Jan 9, 2014" />
              </ListItem>
              <Divider variant="inset" component="li" />
            </>
          ))}
        </List>
        <Box sx={{ height: "20%", width: "100%" }}>
          <Grid
            container
            spacing={{ xs: 1, md: 1 }}
            columns={{ xs: 4, sm: 8, md: 12 }}
          >
            <Grid item xs={6} md={12}>
              <Item>xs=6 md=8</Item>
            </Grid>
            {Array.from(Array(6)).map((_, index) => (
              <Grid item xs={2} sm={4} md={4} key={index}>
                <Item>xs=2</Item>
              </Grid>
            ))}
          </Grid>
        </Box>
      </Box>
    </Stack>
  );
};

export default StaffMainPage;
