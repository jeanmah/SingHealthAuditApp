import React, { Component } from 'react';
import AuditorHomeService from '../../service/home_pages/AuditorHomeService';

import { Link } from 'react-router-dom';

class AuditorHomeComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            idkwhatyet: null,
            message: null
        }
        this.refreshAuditorHome = this.refreshAuditorHome.bind(this);
    }

    componentDidMount() {
        this.refreshAuditorHome();
    }

    refreshAuditorHome() {
        AuditorHomeService.loadAuditorHomePage()
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
                <h3>Auditor Home Page</h3>
                <div className="container">
            
                    <p>{ this.state.idkwhatyet}</p>
                
                    <Link to="/a/alltenants" id="alltenantsButton" className="btn btn-primary">All tenants</Link>
                </div>
            </div>
        )
    }
}

export default AuditorHomeComponent