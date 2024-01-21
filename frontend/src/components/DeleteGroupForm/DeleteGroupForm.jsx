import React from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";

const DeleteGroupForm = ({ isHidden, setIsHidden, selectedGroups, onDelete }) => {
    async function deleteGroup(group) {
        await CsrfFetch(`${await CentralServerLinksProvider.getLink("group-delete")}/${group.groupID}`, {
            method: 'delete'
        });
    }

    async function deleteGroups() {
        for (let group of selectedGroups)
            await deleteGroup(group);
        setIsHidden(true);
        await onDelete();
    }

    return (
        <PopUpForm header={`Вы действительно хотите удалить выбранные группы?`} isHidden={isHidden}>
            <Button text={'Удалить'} onClick={deleteGroups}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default DeleteGroupForm;