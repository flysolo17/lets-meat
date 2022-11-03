import { Stack, Typography } from "@mui/material";
import LockIcon from "@mui/icons-material/Lock";
interface StaffCardProps {}

const StaffCard: React.FunctionComponent<StaffCardProps> = () => {
  return (
    <>
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
        <img
          className="staff"
          src="https://sm.askmen.com/askmen_in/article/f/facebook-p/facebook-profile-picture-affects-chances-of-gettin_gstt.jpg"
        />
        <Typography
          component={"h1"}
          variant={"h6"}
          sx={{ textAlign: "center" }}
        >
          Sample Staff
        </Typography>
        <LockIcon />
      </Stack>
    </>
  );
};

export default StaffCard;
