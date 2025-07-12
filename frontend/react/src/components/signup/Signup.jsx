import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack, Text} from "@chakra-ui/react";
import CreateCustomerForm from "../sharde/CreateCustomerForm.jsx";

const Signup = () => {
    const { customer, setCustomFromToken } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if(customer){
            navigate("/dashboard/customers");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        rounded={'full'}
                        src={'https://imgcdn.stablediffusionweb.com/2024/3/20/ca88a823-6e4a-44bd-bade-a5e74472a212.jpg'}
                        boxSize={"250px"}
                        alt={"MoBa Logo"}
                    />
                    <Heading fontSize={'2xl'} mb={15}>Register</Heading>
                    <CreateCustomerForm onSuccess={(token) =>{
                        localStorage.setItem("access_token", token);
                        setCustomFromToken()
                        navigate("/dashboard");
                    }}/>
                    <Link color={"blue.500"} href={"/"}>
                        Have an account? Login
                    </Link>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                padding={10}
                flexDirection={'column'}
                alignItems={'center'}
                justifyContent={'center'}
                bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}>
                <Text fontSize={'6xl'} color={'white'} fontWeight={'bold'} mb={5}>
                    <Link href="#">
                        Register Now
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://user-images.githubusercontent.com/40702606/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png'
                    }
                />
            </Flex>
        </Stack>
    );
}

export default Signup