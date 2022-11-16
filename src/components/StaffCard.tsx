import { Stack, Typography, Grid, Avatar } from "@mui/material";
import LockIcon from "@mui/icons-material/Lock";
import { Staff } from "../models/Staff";
interface StaffCardProps {
  staff: Staff;
}

const StaffCard: React.FunctionComponent<StaffCardProps> = (props) => {
  const { staff } = props;
  return (
    <Stack
      sx={{
        width: 150,
        height: 200,
        padding: 1,
        alignItems: "center",
        "&:hover": {
          backgroundColor: "#D6F7A9",
          boxShadow: "none",
          borderRadius: "10px",
        },
      }}
      spacing={1}
    >
      <Avatar
        src={staff.profile}
        variant="rounded"
        sx={{ width: 100, height: 100 }}
      />

      <Typography component={"h1"} variant={"h6"} sx={{ textAlign: "center" }}>
        {staff.displayName}
      </Typography>
      <LockIcon />
    </Stack>
  );
};

export default StaffCard;
