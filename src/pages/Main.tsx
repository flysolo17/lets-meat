import { Box, Button, Stack, Typography } from "@mui/material";
import { Container } from "@mui/system";
import { useAuth } from "../auth/AuthContext";
import StaffCard from "../components/StaffCard";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import LogoutIcon from "@mui/icons-material/Logout";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import PinView from "../components/PinView";
import React, { useState } from "react";
interface MainPagePops {}

const MainPage: React.FunctionComponent<MainPagePops> = () => {
  const { logout } = useAuth();
  const [user, setUser] = useState();
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
        <Stack direction={"column"}>
          <Stack direction={"row"} spacing={3}>
            <StaffCard />
            <StaffCard />
            <StaffCard />
            <StaffCard />
            <StaffCard />
          </Stack>
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

          <PinView adminPin="1234" />
        </Stack>
      </Box>
    </Stack>
  );
};

export default MainPage;
