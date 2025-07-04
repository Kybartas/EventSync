import {useState} from "react";
import {service} from "../service";

type Props = {
    close: () => void;
}

export function CreateEvent(props: Props) {

    const [title, setTitle] = useState<string>('');
    const [description, setDescription] = useState<string>('');

    const handleCreateEvent = async () => {

        await service.createEvent(title, description);
        props.close();
    }

    return (
        <div className="component">

            <h1>Create event</h1>

            <label>
            Title
                <input type="text" value={title} onChange={(e) => setTitle(e.target.value)}/>
            </label>
            <label>
                Description
                <input type="text" value={description} onChange={(e) => setDescription(e.target.value)}/>
            </label>

            <div className="bottom-buttons">
                <button className="button" onClick={props.close}>
                    Cancel
                </button>
                <button className="button" onClick={handleCreateEvent}>
                    Confirm
                </button>
            </div>
        </div>
    )
}