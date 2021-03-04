import React, { Component } from 'react'
import AuditorTenantService from '../../service/auditorServices/AuditorTenantService.js';
import { Link } from 'react-router-dom';

export const CHECKLISTSTARTCATEGORY = 'Professionalism'

class AuditorTenantComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            currentTenantId: this.props.match.params.id,
            tenant: [],
            message: null
        }

        this.refreshTenant = this.refreshTenant.bind(this);
    }

    componentDidMount() {
        this.refreshTenant();
    }

    refreshTenant() {
        AuditorTenantService.retreveSingleTenant(this.state.currentTenantId)
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ tenant: response.data.tenant})
                }
            )
            .catch(err => console.log("error " + err));

    }

    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>Tenant {this.state.tenant.first_name} {this.state.tenant.last_name}</h3>
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Store Address</th>
                                <th>FB/NFB</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                <tr key={this.state.tenant.acc_id}>
                                    <td>{this.state.tenant.employee_id}</td>
                                    <td>{this.state.tenant.first_name} {this.state.tenant.last_name}</td>
                                    <td>{this.state.tenant.store_addr}</td>
                                    <td>{this.state.tenant.FB_NFB}</td>
                                </tr>
                        
                            }
                            {/* adding appropriate checklist buttons */}
                            <tr></tr>
                            <tr>
                            {(() => {
                                if (this.state.tenant.FB_NFB == "FB") {
                                return (
                                    <td><Link to={"/a/auditchecklist/"+this.state.tenant.acc_id+"/fbchecklist/" + CHECKLISTSTARTCATEGORY} className="btn btn-primary">Start Food and Beverage Audit</Link></td>
                                )
                        } else {
                                return (
                                    <td><Link to={"/a/auditchecklist/"+this.state.tenant.acc_id+"/nfbchecklist/" + CHECKLISTSTARTCATEGORY} className="btn btn-primary">Start Non Food and Beverage Audit</Link></td>
                                )
                                }
                            })()}
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default AuditorTenantComponent
