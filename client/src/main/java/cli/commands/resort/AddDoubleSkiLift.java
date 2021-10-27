package cli.commands.resort;

import stubs.resort.ResortService;

import java.time.Duration;
import java.util.List;

public class AddDoubleSkiLift extends AddSkiLift {

    private String timeLimit;

    @Override
    public String identifier() {
        return "addDoubleSkiLift";
    }

    @Override
    public void execute() throws Exception {
        ResortService rs = shell.system.resortService;
        rs.addDoubleSkiLift(rs.findResortByName(resortName),liftName,open,timeLimit);
    }

    @Override
    public String describe() {
        return "<resortName> <liftName> <isOpen> <timeMinuteLimit> # to add a ski lift to a resort";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
        this.liftName = args.get(1);
        this.open = Boolean.parseBoolean(args.get(2));
        this.timeLimit = Duration.ofMinutes(Long.parseLong(args.get(3))).toString();
    }
}
