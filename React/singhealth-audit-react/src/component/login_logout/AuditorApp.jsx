import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import AuditorHomeComponent from '../home/AuditorHomeComponent';
import AuditorTenantsComponent from '../AuditorComponents/AuditorTenantsComponent';
import AuditorTenantComponent from '../AuditorComponents/AuditorTenantComponent';
import AuditChecklistComponent from '../AuditorComponents/AuditChecklistComponent';

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
                            <AuthenticatedRoute path="/a/alltenants" exact component={AuditorTenantsComponent}/>
                            <AuthenticatedRoute path="/a/tenant/:id" exact component={AuditorTenantComponent}/>
                            <AuthenticatedRoute path="/a/auditchecklist/:tenantid/:checklistType/:checklistcategory" exact component={AuditChecklistComponent}/>
                            
                        </Switch>
                    </>
                </Router>
            </>
        )
    }
}

export default AuditorApp