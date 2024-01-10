import React from 'react';
import styles from './style.module.css'
import {useNavigate} from "react-router-dom";

const SidebarItem = ({ link, text, src }) => {
    //const navigate = useNavigate();

    function onClick() {
        //navigate(link);
    }

    return (
        <div className={styles.sidebarItem} onClick={onClick}>
            <img className={styles.sidebarItemIcon} src={src} alt={'Icon'}/>
            <p className={styles.sidebarItemText}> {text} </p>
        </div>
    );
};

export default SidebarItem;