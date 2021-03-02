import React, { Component } from 'react'
import AuditorTenantService from '../../service/auditorServices/AuditorTenantService.js';
import { Link } from 'react-router-dom';


class AuditorTenantsComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            tenants: [],
            message: null
        }
        this.refreshTenants = this.refreshTenants.bind(this);
    }

    componentDidMount() {
        this.refreshTenants();
    }

    refreshTenants() {
        AuditorTenantService.retrieveAllTenants()
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ tenants: response.data.tenants})
                }
            )
            .catch(err => console.log("error " + err));

    }

    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>All tenants</h3>
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>FB/NFB</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.tenants.map(
                                    tenant =>
                                        <tr key={tenant.acc_id}>
                                            <td>{tenant.employee_id}</td>
                                            <td>{tenant.first_name} {tenant.last_name}</td>
                                            <td>{tenant.FB_NFB}</td>
                                            <td><Link to={"/a/tenant/"+tenant.acc_id} id={"audit_button_"+tenant.acc_id} 
                                            className="btn btn-primary">Audit</Link></td>
                                            
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default AuditorTenantsComponent
