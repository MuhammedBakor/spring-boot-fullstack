
import SidebarWithHeader from "./components/sharde/sidebar.jsx";
import {Text} from "@chakra-ui/react";

function Home() {
  return (
      <SidebarWithHeader>
          <Text fontSize={"6xl"}>Dashboard</Text>
      </SidebarWithHeader>
  )
}

export default Home;
