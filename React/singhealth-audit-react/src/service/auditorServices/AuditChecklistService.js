import axios from 'axios'

import AuthenticationService from '../login_logout/AuthenticationService';

const AUDITORS_API_URL = 'http://localhost:8080'

class AuditChecklistService {

    getcategoryQuestions(tenantid,checklistType,checklistcategory) {
        //console.log('executed service')
        AuthenticationService.getStoredAxiosInterceptor();
        return axios.get(`${AUDITORS_API_URL}/a/auditchecklist/${tenantid}/${checklistType}/${checklistcategory}`);
    }

    //TODO: implement this whenever changing pages 
    saveCurrentCategory(){
        
    }

}

export default new AuditChecklistService()