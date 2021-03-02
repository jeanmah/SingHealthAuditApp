import axios from 'axios'
import AuthenticationService from '../../service/login_logout/AuthenticationService';

const AUDITORS_API_URL = 'http://localhost:8080'

class AuditorHomeService {

    loadAuditorHomePage() {
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor()
        return axios.get(`${AUDITORS_API_URL}/home/a`,
        
        );
    }
}

export default new AuditorHomeService()