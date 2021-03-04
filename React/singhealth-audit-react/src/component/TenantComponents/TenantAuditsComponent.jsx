import React, { Component } from 'react'
import tenantAuditService from '../../service/tenantServices/tenantAuditService.js';
import { Link } from 'react-router-dom';


class TenantAuditsComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            allAudits: [],
            message: null
        }
        this.refreshAllAduits= this.refreshAllAduits.bind(this);
    }

    componentDidMount() {
        this.refreshAllAduits();
    }

    refreshAllAduits() {
        tenantAuditService.getAllAudits()
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ allAudits: response.data.openAudits})
                }
            )
            .catch(err => console.log("error " + err));
    }

    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>Incomplete audits</h3>
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Report ID</th>
                                <th>Date</th>
                                <th>Score</th>
                            </tr>
                        </thead>
                        <tbody>
                            {(() => {
                                if (this.state.allAudits=== "no audits") {
                                    return (
                                        <tr><td>No past audits</td></tr>
                                    )
                                } else {
                                    return (
                                            this.state.allAudits.map(
                                                audit =>
                                                    <tr key={audit}>
                                                        <td>{audit.report_id}</td>
                                                        <td>{audit.start_date}</td>
                                                        <td>{audit.overall_score}</td>
                                                        <td><Link to={"t/view/pastaudit/" +audit.report_id} id={"viewAuditButton"+audit.report_id} 
                                                        className="btn btn-primary">view audit</Link></td>
                                                        
                                                    </tr>
                                            )                             
                                        )
                                }
                            })()}
                        
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

export default TenantAuditsComponent
