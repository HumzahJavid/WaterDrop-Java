import java.util.List;
// this class is required to be able to extract List<List<Pipe>> from the gson string. 
// https://stackoverflow.com/questions/27253555/com-google-gson-internal-linkedtreemap-cannot-be-cast-to-my-class
public class WrapperOfPipes{
    private List<List<Pipe>> wrappedPipes;
    WrapperOfPipes(){
    }

    WrapperOfPipes(List<List<Pipe>> pipes){
        this.wrappedPipes = pipes;
    }
    public List<List<Pipe>> getWrappedPipes(){
        return this.wrappedPipes;
    }
	
    @Override
    public String toString() {
        System.out.println("Grid of pipes");
        String string = "";
        for (int i = 0; i < this.wrappedPipes.size(); i++) {
            string += "\n";
            string += this.wrappedPipes.get(i).toString();
        }
        return string;
    }
}