import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import Header from "../Header/Header";
import Content from "../Content/Content";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import {useNavigate} from "react-router-dom";

const PrivateRoute = () => {
    const navigate = useNavigate();
    const [authentication, setAuthentication] = useState(null);

    useEffect(() => {
        async function getAuthentication() {
            let newAuthentication, response = await CsrfFetch(await CentralServerLinksProvider.getLink('auth-get'), {
                method: 'get'
            });
            
            if (response.ok) {
                newAuthentication = await response.json();
                setAuthentication(newAuthentication);
                if (!newAuthentication.isAuthenticated)
                    navigate('/login');
            }
        }
        
        void getAuthentication();
    }, [navigate]);

    return (
        <div className={styles.privateRoute}>
            <Header authentication={authentication}/>
            <Content/>
        </div>
    );
};

export default PrivateRoute;