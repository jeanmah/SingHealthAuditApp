import axios from 'axios'
import AuthenticationService from '../login_logout/AuthenticationService';

const AUDITORS_API_URL = 'http://localhost:8080'

class HomeService {

    loadHomePage(accountType) {
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor()
        return axios.get(`${AUDITORS_API_URL}/home/${accountType}`,
        );
    }
}

export default new HomeService()