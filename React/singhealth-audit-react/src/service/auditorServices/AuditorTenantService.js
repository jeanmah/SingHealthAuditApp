import axios from 'axios'

import AuthenticationService from '../login_logout/AuthenticationService';

const AUDITORS_API_URL = 'http://localhost:8080'

class AuditorTenantService {

    retrieveAllTenants() {
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor();
        return axios.get(`${AUDITORS_API_URL}/a/alltenants`);
    }

    retreveSingleTenant(tenant_accId){
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor();
        return axios.get(`${AUDITORS_API_URL}/a/tenant/${tenant_accId}`);
    }

}

export default new AuditorTenantService()