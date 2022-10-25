import { useAuth } from "../auth/AuthContext";
interface MainPagePops {}

const MainPage: React.FunctionComponent<MainPagePops> = () => {
  const { logout } = useAuth();

  return (
    <>
      main
      <button onClick={logout}>Logout</button>
    </>
  );
};

export default MainPage;
