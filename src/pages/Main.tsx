import { Box, Button, Stack, Typography } from "@mui/material";
import { Container } from "@mui/system";
import { useAuth } from "../auth/AuthContext";
import StaffCard from "../components/StaffCard";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
interface MainPagePops {}

const MainPage: React.FunctionComponent<MainPagePops> = () => {
  const { logout } = useAuth();

  return (
    <Stack
      direction={"column"}
      sx={{
        width: "100%",
        height: "100vh",
      }}
    >
      <Button
        variant="text"
        startIcon={<ArrowBackIcon />}
        sx={{ width: "200px", color: "black" }}
        onClick={logout}
      >
        Logout
      </Button>
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
          <Typography component={"h2"} variant={"h3"}>
            {" "}
            Frozen Meat Shop
          </Typography>
          <Typography component={"h2"} variant={"h6"}>
            by John Mark Ballangca
          </Typography>
        </Stack>
        <Stack direction={"column"}>
          <Typography variant="h4" component={"h2"} marginLeft={3}>
            Staff Login
          </Typography>
          <Typography
            variant="h6"
            component={"h2"}
            marginLeft={3}
            color={"text.sencondary"}
          >
            Do not share you pin.
          </Typography>
          <Stack direction={"row"} spacing={3}>
            <StaffCard />
            <StaffCard />
            <StaffCard />
            <StaffCard />
            <StaffCard />
          </Stack>
        </Stack>

        <Stack>
          <Button color="error">Login as Admin</Button>
        </Stack>
      </Box>
    </Stack>
  );
};

export default MainPage;
