import HomeIcon from "@mui/icons-material/Home";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import InfoIcon from "@mui/icons-material/Info";
import InventoryIcon from "@mui/icons-material/Inventory";
import DashboardIcon from "@mui/icons-material/Dashboard";
import GroupsIcon from "@mui/icons-material/Groups";
export const navData = [
  {
    title: "Dashboard",
    path: "/dashboard",
    icon: <DashboardIcon />,
  },
  {
    title: "Inventory",
    path: "/inventory",
    icon: <InventoryIcon />,
  },
  {
    title: "Staffs",
    path: "/staff",
    icon: <GroupsIcon />,
  },
  {
    title: "Account",
    path: "/account",
    icon: <AccountCircleIcon />,
  },
];
