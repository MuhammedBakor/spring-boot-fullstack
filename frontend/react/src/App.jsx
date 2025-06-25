
import SidebarWithHeader from "./components/sharde/sidebar.jsx";
import {Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import {getCustomers} from "./services/client.js";
import {useEffect, useState} from "react";
import CardWithImage from "./components/Card.jsx";
import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";

function App() {

    const [customers, setCustomers] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState("")

    const fetchCustomers = () => {
        setLoading(true)
        getCustomers()
            .then( res => {
                setCustomers(res.data)}
            ).catch(err => {
                setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )})
            .finally(() => setLoading(false))
    }

    useEffect(() => {
        fetchCustomers();
    }, [])

    if (loading) {
        return (
            <Spinner color="teal.500" size="lg" />
        )
    }

    if (error) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>Ooops there was an error</Text>
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>No data avalibal</Text>
            </SidebarWithHeader>
        )
    }

  return (
      <SidebarWithHeader>
          <CreateCustomerDrawer
              fetchCustomers={fetchCustomers}
          />
              <Wrap justify="center" spacing={30}>
                  {
                      customers.map((customer, index) => (
                  <WrapItem key={index} >
                      <CardWithImage
                          imageNumber={index}
                          {...customer}
                      fetchCustomers={fetchCustomers}
                      />
                  </WrapItem>
                  ))
                  }
              </Wrap>
      </SidebarWithHeader>
  )
}

export default App
