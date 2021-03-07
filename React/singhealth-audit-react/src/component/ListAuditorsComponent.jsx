import React, { Component } from 'react'
import AuditorDataService from '../../service/login_logout/AuditorDataService.js';


class ListAuditorsComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            auditors: [],
            message: null
        }
        this.refreshAuditors = this.refreshAuditors.bind(this)
    }

    componentDidMount() {
        this.refreshAuditors();
    }

    refreshAuditors() {
        AuditorDataService.retrieveAllAuditors()
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ auditors: response.data })
                }
            )
            .catch(err => console.log("error " + err));

    }


    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>All auditors</h3>
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.auditors.map(
                                    auditor =>
                                        <tr key={auditor.id}>
                                            <td>{auditor.id}</td>
                                            <td>{auditor.username}</td>
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

export default ListAuditorsComponent
