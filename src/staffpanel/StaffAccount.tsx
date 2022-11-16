import { useNavigate } from "react-router-dom";
interface StaffAccountPageProps {}

const StaffAccountPage: React.FunctionComponent<StaffAccountPageProps> = () => {
  const navigate = useNavigate();
  const staffLogout = () => {
    localStorage.clear();
    navigate("/");
  };
  return (
    <>
    
          
          
      <button onClick={staffLogout}>Logout</button>
    </>
  );
};

export default StaffAccountPage;
