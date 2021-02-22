import axios from 'axios'
import AuthenticationService from '../../service/login_logout/AuthenticationService';

const AUDITORS_API_URL = 'http://localhost:8080'

class AuditorDataService {

    retrieveAllAuditors() {
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor()
        return axios.get(`${AUDITORS_API_URL}/auditors`,
        
        );
    }
}

export default new AuditorDataService()