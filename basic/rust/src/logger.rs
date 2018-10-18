
extern crate simplelog;

fn simplelog() {

    use simplelog::*;
    use std::fs::File;

    CombinedLogger::init(
        vec![
            TermLogger::new(LevelFilter::Debug, Config::default()).unwrap(),
            WriteLogger::new(LevelFilter::Debug, Config::default(),
                             File::create("output.log").unwrap()),
        ]
    ).unwrap();

    info!("simple log init");
}