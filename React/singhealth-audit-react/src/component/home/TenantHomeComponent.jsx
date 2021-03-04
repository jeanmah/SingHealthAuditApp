import React, { Component } from 'react';
import HomeService from '../../service/home_pages/HomeService';

import { Link } from 'react-router-dom';

class TenantHomeComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            idkwhatyet: null,
            message: null
        }
        this.refreshTenantHome = this.refreshTenantHome.bind(this);
    }

    componentDidMount() {
        this.refreshTenantHome();
    }

    refreshTenantHome() {
        HomeService.loadHomePage('t')
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ idkwhatyet: response.data })
                }
            )
            .catch(err => console.log("error " + err));

    }
    
    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>Tenant Home Page</h3>
                <div className="container">
                    <p>Welcome {this.state.idkwhatyet}</p>
                    <Link to="/t/view/pastaudits" id="allpastauditsButton" className="btn btn-primary">View latest incomplete audit</Link>
                </div>
            </div>
        )
    }
}

export default TenantHomeComponent