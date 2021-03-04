import axios from 'axios'

import AuthenticationService from '../login_logout/AuthenticationService';

const AUDITORS_API_URL = 'http://localhost:8080'

class tenantAuditService {

    getAllAudits() {
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor();
        return axios.get(`${AUDITORS_API_URL}/t/view/pastaudits`);
    }

    getOneAudit(auditId){
        AuthenticationService.getStoredAxiosInterceptor();
        return axios.get(`${AUDITORS_API_URL}/t/view/pastaudit/${auditId}`);
    }

}

export default new tenantAuditService()