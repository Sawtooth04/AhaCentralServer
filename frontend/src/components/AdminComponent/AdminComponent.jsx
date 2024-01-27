import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import AccessDeniedRedirectForm from "../AccesDeniedRedirectForm/AccessDeniedRedirectForm";

const AdminComponent = ({ children }) => {
    const [isAdmin, setIsAdmin] = useState(true);
    const [isNotAdminFormHidden, setIsNotAdminFormHidden] = useState(true);

    useEffect(() => {
        async function getRole() {
            let response = await CsrfFetch((await CentralServerLinksProvider.getLink('customer-role-get')).replace('{role}', 'admin'), {
                method: 'get'
            });

            setIsAdmin((await response.json()).isHaveRole);
        }

        void getRole();
    }, []);

    useEffect(() => {
        if (!isAdmin)
            setIsNotAdminFormHidden(false);
    }, [isAdmin]);

    return (
        <div className={styles.adminComponent}>
            <AccessDeniedRedirectForm isHidden={isNotAdminFormHidden}/>
            {children}
        </div>
    );
};

export default AdminComponent;