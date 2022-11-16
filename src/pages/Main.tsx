import { Box, Button, Stack, Typography } from "@mui/material";

import { useAuth } from "../auth/AuthContext";
import StaffCard from "../components/StaffCard";
import Avatar from "@mui/material/Avatar";
import LogoutIcon from "@mui/icons-material/Logout";

import PinView from "../components/PinView";
import React, { useState, useEffect } from "react";

import { collection, doc, onSnapshot } from "firebase/firestore";
import { firestore } from "../config/config";
import LockIcon from "@mui/icons-material/Lock";
import { experimentalStyled as styled } from "@mui/material/styles";

import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import { STAFF_ID, STAFF_TABLE } from "../utils/Constants";
import { userConverter, Users } from "../auth/models/Users";
import StaffPinView from "../components/StaffPinView";
import { useNavigate } from "react-router-dom";
const Item = styled(Container)(({ theme }) => ({
  padding: theme.spacing(2),
  textAlign: "center",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  flexDirection: "column",
  "&:hover": {
    backgroundColor: "#D6F7A9",
    boxShadow: "none",
    borderRadius: "10px",
  },
}));
interface MainPagePops {}

const MainPage: React.FunctionComponent<MainPagePops> = () => {
  const { logout, currentUser } = useAuth();
  const navigate = useNavigate();
  const [staff, setStaff] = useState<any[]>([]);
  const [userInfo, setUserInfo] = useState<Users>({} as Users);

  useEffect(() => {
    if (currentUser != null) {
      const docRef = doc(firestore, "Users", currentUser.uid).withConverter(
        userConverter
      );
      const unsub = onSnapshot(docRef, (snapshot) => {
        if (snapshot.exists()) {
          let user: Users = snapshot.data();
          setUserInfo(user);
          console.log(user);
        }
      });
      return () => unsub();
    }
  }, []);
  useEffect(() => {
    const staffID = localStorage.getItem(STAFF_ID);
    if (staffID != null) {
      navigate("/cashier");
    }
  }, []);
  useEffect(() => {
    const ref = collection(firestore, STAFF_TABLE);
    const unsub = onSnapshot(ref, (snapshot) => {
      let data: any[] = [];
      snapshot.forEach((doc) => {
        data.push({ ...doc.data(), id: doc.id });
      });
      setStaff(data);
      console.log("Staff fetching success..");
    });
    return () => unsub();
  }, []);

  return (
    <Stack
      direction={"column"}
      sx={{
        width: "100%",
        height: "100vh",
      }}
    >
      <Box
        sx={{
          width: "100%",
          height: "100%",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          flexDirection: "column",
        }}
        padding={5}
      >
        <Stack direction={"column"} alignItems={"center"}>
          <Typography
            sx={{
              fontFamily: "Poppins",
              fontSize: 40,
              fontWeight: 700,
              fontStyle: "bold",
            }}
          >
            {" "}
            JJF FROZEN FOODS
          </Typography>
          <Typography component={"h2"} variant={"h6"}>
            powered by Let's Meat
          </Typography>
        </Stack>
        <Stack direction={"row"} spacing={1} sx={{ width: "50%" }}>
          <Box sx={{ flexGrow: 1 }}>
            <Grid
              container
              spacing={{ xs: 2, md: 3 }}
              columns={{ xs: 4, sm: 8, md: 18 }}
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              {staff.map((row) => (
                <Grid item xs={2} sm={4} md={4} key={row.id} spacing={1}>
                  <Item>
                    <StaffPinView row={row} id={row.id} />
                  </Item>
                </Grid>
              ))}
            </Grid>
          </Box>
        </Stack>
        <Stack direction={"row"} spacing={2}>
          <Stack
            direction={"column"}
            spacing={1}
            onClick={logout}
            sx={{
              padding: 1,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              "&:hover": {
                backgroundColor: "#D6F7A9",
                boxShadow: "none",
                borderRadius: "10px",
              },
            }}
          >
            <LogoutIcon sx={{ width: 50, height: 50 }} />
            <Typography
              sx={{
                fontFamily: "Poppins",
                fontSize: 20,
                fontWeight: 700,
                fontStyle: "bold",
              }}
            >
              Logout
            </Typography>
          </Stack>

          <PinView adminPin={userInfo.branchCode} />
        </Stack>
      </Box>
    </Stack>
  );
};

export default MainPage;
