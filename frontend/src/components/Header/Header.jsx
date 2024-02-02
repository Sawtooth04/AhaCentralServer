import React from 'react';
import styles from './style.module.css'
import {useNavigate} from "react-router-dom";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const Header = ({ authentication }) => {
    const navigate = useNavigate();

    async function logout() {
        await CsrfFetch(await CentralServerLinksProvider.getLink('auth-logout'), { method: 'post'});
        navigate('/login');
    }

    return (
        <header className={styles.header}>
            <img className={styles.headerLogo} src={"assets/images/text-logo.png"} alt={"Logo"}/>
            <div className={styles.headerCustomer}>
                <p className={styles.headerCustomerText} onClick={logout}> {authentication?.name} </p>
                <p className={styles.headerCustomerHint}> Выйти </p>
            </div>
        </header>
    );
};

export default Header;