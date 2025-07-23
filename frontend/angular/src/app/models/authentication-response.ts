import {CustomerDTO} from './Customer-dto';

export interface AuthenticationResponse {

  token?: string;
  customerDTO: CustomerDTO;
}
