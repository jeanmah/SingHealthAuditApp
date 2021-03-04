import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import AuditorHomeComponent from '../home/AuditorHomeComponent';
import TenantHomeComponent from '../home/TenantHomeComponent';
import ManagerHomeComponent from '../home/ManagerHomeComponent';
import AuditorTenantsComponent from '../AuditorComponents/AuditorTenantsComponent';
import AuditorTenantComponent from '../AuditorComponents/AuditorTenantComponent';
import AuditChecklistComponent from '../AuditorComponents/AuditChecklistComponent';
import TenantAuditsComponent from '../TenantComponents/TenantAuditsComponent';
import TenantAuditComponent from '../TenantComponents/TenantAuditComponent';

import MenuComponent from './MenuComponent';
import AuthenticatedRoute from './AuthenticatedRoute';

class AuditorApp extends Component {

    render() {
        return (
            <>
                <Router>
                    <>
                        <MenuComponent />
                        <Switch>
                            <Route path="/" exact component={LoginComponent} />
                            <Route path="/login" exact component={LoginComponent} />
                            <AuthenticatedRoute path="/logout" exact component={LogoutComponent} />
                            <AuthenticatedRoute path="/home/a" exact component={AuditorHomeComponent} />
                            <AuthenticatedRoute path="/home/t" exact component={TenantHomeComponent} />
                            <AuthenticatedRoute path="/home/m" exact component={ManagerHomeComponent} />
                            <AuthenticatedRoute path="/a/alltenants" exact component={AuditorTenantsComponent}/>
                            <AuthenticatedRoute path="/a/tenant/:id" exact component={AuditorTenantComponent}/>
                            <AuthenticatedRoute path="/a/auditchecklist/:tenantid/:checklistType/:checklistcategory" exact component={AuditChecklistComponent}/>
                            <AuthenticatedRoute path="/t/view/pastaudits" exact component={TenantAuditsComponent}/>
                            <AuthenticatedRoute path="t/view/pastaudit/:auditId" exact component={TenantAuditComponent}/>
                        </Switch>
                    </>
                </Router>
            </>
        )
    }
}

export default AuditorApp