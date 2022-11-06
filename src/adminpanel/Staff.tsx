import { Container, Stack } from "@mui/material";
import React, { useEffect, useState } from "react";
import AddStaffDialog from "../dialog/AddStaff";
import { experimentalStyled as styled } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Grid from "@mui/material/Grid";
import Divider from "@mui/material/Divider";
import Typography from "@mui/material/Typography";
import { Staff } from "../models/Staff";
import StaffCardInfo from "../components/StaffCardInfo";
import { collection, onSnapshot, query } from "firebase/firestore";
import { firestore } from "../config/config";
import { STAFF_TABLE } from "../utils/Constants";

interface StaffPageProps {}

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
  ...theme.typography.body2,
  textAlign: "center",
  color: theme.palette.text.secondary,
  "&:hover": {
    backgroundColor: "#D6F7A9",
    boxShadow: "none",
    borderRadius: "10px",
  },
}));

const StaffPage: React.FunctionComponent<StaffPageProps> = (props) => {
  const [staffs, setStaffs] = useState<any[]>();

  useEffect(() => {
    const q = query(collection(firestore, STAFF_TABLE));
    const unsubscribe = onSnapshot(q, (querySnapshot) => {
      const data: any[] = [];
      querySnapshot.forEach((doc) => {
        data.push({ ...doc.data(), id: doc.id });
      });
      setStaffs(data);
    });
    return () => unsubscribe();
  }, []);
  return (
    <Stack sx={{ padding: 3 }} spacing={2}>
      <Box
        sx={{
          flexGrow: 1,
          display: "flex",
          alignItems: "center",
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
          JJF Staffs
        </Typography>
        <AddStaffDialog />
      </Box>
      <Divider />
      <Box sx={{ flexGrow: 1 }}>
        <Grid
          container
          spacing={{ xs: 2, md: 3 }}
          columns={{ xs: 4, sm: 8, md: 12 }}
        >
          {staffs?.map((data) => (
            <Grid item xs={2} sm={4} md={4} key={data.id}>
              <Item>
                <StaffCardInfo staff={data} id={data.id} />
              </Item>
            </Grid>
          ))}
        </Grid>
      </Box>
    </Stack>
  );
};

export default StaffPage;
