import React, {useEffect, useRef, useState} from 'react';
import styles from './style.module.css';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";
import StorageServerStatusListItem from "../StorageServerStatusListItem/StorageServerStatusListItem";

const AddStorageServerForm = ({ isHidden, setIsHidden, onAdd }) => {
    const [storageServerStatuses, setStorageServerStatuses] = useState([]);
    const [selectedStorageServerStatus, setSelectedStorageServerStatus] = useState(null);
    const nameRef = useRef(null);
    const addressRef = useRef(null);

    useEffect(() => {
        async function getStorageServerStatuses() {
            let response = await CsrfFetch(await CentralServerLinksProvider.getLink('storage-server-status-get'), {
                method: 'get'
            });
            setStorageServerStatuses((await response.json()).statuses);
        }

        void getStorageServerStatuses();
    }, []);

    function clear() {
        setSelectedStorageServerStatus([]);
        nameRef.current.value = '';
        addressRef.current.value = '';
    }

    async function addStorageServer() {
        if (selectedStorageServerStatus !== null) {
            await CsrfFetch(await CentralServerLinksProvider.getLink('storage-server-post'), {
                method: 'post',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    'name': nameRef.current.value,
                    'address': addressRef.current.value,
                    'storageServerStatusID': selectedStorageServerStatus.storageServerStatusID
                })
            });
            await onAdd();
            setIsHidden(true);
            clear();
        }
    }

    function isStorageServerStatusSelected(storageServerStatus) {
        return selectedStorageServerStatus !== null && selectedStorageServerStatus.name === storageServerStatus.name;
    }

    function onStorageServerStatusClick(storageServerStatus) {
        if (isStorageServerStatusSelected(storageServerStatus))
            setSelectedStorageServerStatus(null);
        else
            setSelectedStorageServerStatus(storageServerStatus);
    }

    return (
        <div className={styles.addStorageServerForm}>
            <PopUpForm header={'Добавление сервера хранения'} isHidden={isHidden}>
                <TextInput type={'text'} placeholder={'Название'} inputRef={nameRef}/>
                <TextInput type={'text'} placeholder={'Адрес'} inputRef={addressRef}/>
                <div className={styles.storageServerStatusList}>
                    <p className={styles.storageServerStatusListHeader}> Статус сервера </p>
                    {storageServerStatuses.map((item) => {
                        return <StorageServerStatusListItem item={item} onClick={onStorageServerStatusClick}
                            isSelected={isStorageServerStatusSelected(item)} key={item.storageServerStatusID}/>
                    })}
                </div>
                <Button text={'Добавить'} onClick={addStorageServer}/>
                <Button text={'Отменить'} onClick={() => { setIsHidden(true); clear(); }}/>
            </PopUpForm>
        </div>
    );
};

export default AddStorageServerForm;