
import SidebarWithHeader from "./components/sharde/sidebar.jsx";
import {Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import {getCustomers} from "./services/client.js";
import {useEffect, useState} from "react";
import CardWithImage from "./components/Card.jsx";

function App() {

    const [customers, setCustomers] = useState([])
    const [loading, setLoading] = useState(false)

    useEffect(() => {console.log("API URL:", import.meta.env.VITE_API_BASE_URL);

        setLoading(true)
        getCustomers()
            .then( res => {
               console.log(res)
                setCustomers(res.data)}
            ).catch(err => {
            console.log(err) })
            .finally(() => setLoading(false))
    }, [])

    if (loading) {
        return (
            <Spinner color="teal.500" size="lg" />
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No data avalibal</Text>
            </SidebarWithHeader>
        )
    }

  return (
      <SidebarWithHeader>
              <Wrap justify="center" spacing={30}>
                  {
                      customers.map((customer, index) => (
                  <WrapItem key={index} >
                      <CardWithImage
                          imageNumber={index}
                          {...customer}/>
                  </WrapItem>
                  ))
                  }
              </Wrap>
      </SidebarWithHeader>
  )
}

export default App
