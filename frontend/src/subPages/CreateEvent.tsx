type Props = {
    title: string;
    description: string;
    setTitle: (title: string) => void;
    setDescription: (description: string) => void;
    onCancel: () => void;
    onConfirm: () => void;
}


export function CreateEvent(props: Props) {
    return (
        <div className="component">

            <h1>Create event</h1>

            <label>
            Title
                <input type="text" value={props.title} onChange={(e) => props.setTitle(e.target.value)}/>
            </label>
            <label>
                Description
                <input type="text" value={props.description} onChange={(e) => props.setDescription(e.target.value)}/>
            </label>

            <div className="bottom-buttons">
                <button className="button" onClick={props.onCancel}>
                    Cancel
                </button>
                <button className="button" onClick={props.onConfirm}>
                    Confirm
                </button>
            </div>
        </div>
    )
}