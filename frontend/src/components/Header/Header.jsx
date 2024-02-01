import React from 'react';
import styles from './style.module.css'

const Header = ({ authentication }) => {
    return (
        <header className={styles.header}>
            <img className={styles.headerLogo} src={"assets/images/text-logo.png"} alt={"Logo"}/>
        </header>
    );
};

export default Header;