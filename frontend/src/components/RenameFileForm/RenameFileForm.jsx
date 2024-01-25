import React, {useRef} from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";
import TextInput from "../UI/TextInput/TextInput";

const RenameFileForm = ({ file, buildPath, isHidden, setIsHidden, onRename, forbidden}) => {
    const fileNameRef = useRef(null);

    async function renameFile() {
        let response = await CsrfFetch(`${await CentralServerLinksProvider.getLink("file-patch")}/${buildPath()}/${file.name}`, {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify([{
                'op': 'replace',
                'path': '/name',
                'value': fileNameRef.current.value
            }])
        });
        if (response.status === 403)
            forbidden();
        else
            await onRename();
        setIsHidden(true);
    }

    return (
        <PopUpForm header={'Переименовать файл'} isHidden={isHidden}>
            <TextInput type={'text'} placeholder={'Имя'} inputRef={fileNameRef}/>
            <Button text={'Применить'} onClick={renameFile}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default RenameFileForm;